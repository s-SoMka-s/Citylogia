using Citylogia.Server.Core.Db.Implementations;
using Citylogia.Server.Core.Entityes;
using Core.Api.Models;
using Libraries.Db.Reposiitory.Interfaces;
using Libraries.GoogleStorage;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Linq;
using System.Threading.Tasks;

namespace Core.Api.Files
{
    [ApiController]
    [Route("/api/Files")]
    public class Main : Controller
    {
        private readonly SqlContext context;
        private readonly ICrudRepository<Photo> photos;
        private readonly ICloudStorage storage;

        public Main(SqlContext context, ICloudStorage storage, ICrudFactory factory)
        {
            this.context = context;
            this.photos = factory.Get<Photo>();
            this.storage = storage;
        }


        [HttpGet("")]
        public async Task<BaseCollectionResponse<FileSummary>> SelectAsync()
        {
            var files = await this.context.Photos.Select(f => new FileSummary(f)).ToListAsync();

            return new BaseCollectionResponse<FileSummary>(files);
        }

        [HttpPost("")]
        public async Task<long> UploadAsync([FromBody] NewFileParameters parameters)
        {
            var res = await this.storage.UploadFileAsync(parameters.Name, parameters.Extension, parameters.Content);

            var @new = new Photo()
            {
                PublicUrl = res.PublicUrl,
                Name = res.Name
            };

            var uplouded = await photos.AddAsync(@new);
            

            return uplouded.Id;
        }
    }
}

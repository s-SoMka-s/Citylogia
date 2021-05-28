using Citylogia.Server.Core.Db.Implementations;
using Core.Api.Models;
using Libraries.GoogleStorage;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json;
using System.Linq;
using System.Threading.Tasks;

namespace Core.Api.Files
{
    [ApiController]
    [Route("/api/Files")]
    public class Main : Controller
    {
        private readonly SqlContext context;
        private readonly ICloudStorage storage;

        public Main(SqlContext context, ICloudStorage storage)
        {
            this.context = context;
            this.storage = storage;
        }


        [HttpGet("")]
        public async Task<BaseCollectionResponse<FileSummary>> SelectAsync()
        {
            var files = await this.context.Photos.Select(f => new FileSummary(f)).ToListAsync();

            return new BaseCollectionResponse<FileSummary>(files);
        }

        [HttpPost("")]
        public async Task<bool> UploadAsync([FromBody] NewFileParameters parameters)
        {
            var link = await this.storage.UploadFileAsync(parameters.Content, "test.png");
            return true;
        }
    }
}

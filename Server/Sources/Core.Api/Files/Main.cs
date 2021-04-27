using Citylogia.Server.Core.Db.Implementations;
using Core.Api.Models;
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

        public Main(SqlContext context)
        {
            this.context = context;
        }


        [HttpGet("")]
        public async Task<BaseCollectionResponse<FileSummary>> SelectAsync()
        {
            var files = await this.context.Photos.Select(f => new FileSummary(f)).ToListAsync();

            return new BaseCollectionResponse<FileSummary>(files);
        }
    }
}

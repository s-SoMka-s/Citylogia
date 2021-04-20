using Citylogia.Server.Core.Db.Implementations;
using Microsoft.AspNetCore.Mvc;

namespace Core.Api
{
    [ApiController]
    [Route("api")]
    public class ReviewController
    {
        private readonly SqlContext context;
        public ReviewController(SqlContext sqlContext)
        {
            this.context = sqlContext;
        }
    }
}

using Citylogia.Server.Core.Entityes;
using Core.Api.Admin.Models.Output;
using Libraries.Db.Reposiitory.Interfaces;
using Microsoft.AspNetCore.Mvc;
using System.Linq;

namespace Core.Api.Admin
{

    [ApiController]
    [Route("/api")]
    public class Main : ApiController
    {
        private ICrudRepository<Place> places;

        public Main(ICrudFactory factory)
        {
            this.places = factory.Get<Place>();
        }

        [HttpGet("Summary")]
        public AppSummary GetAppSummary()
        {
            var newCount = places.Query().Count(p => !p.IsApproved);
            var allCount = places.Query().Count();

            return new AppSummary(newCount, allCount);
        }

    }
}

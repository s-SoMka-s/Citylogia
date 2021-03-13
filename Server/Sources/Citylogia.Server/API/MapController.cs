using Microsoft.AspNetCore.Mvc;
using System.Linq;
using System.Collections.Generic;
using Citylogia.Server.Core.Entityes;
using Citylogia.Server.Core.Db.Implementations;

namespace Citylogia.Server.API
{
    [ApiController]
    [Route("/api/Map/Places")]
    public class MapController : Controller
    {
        public MapController()
        {
        }

        [HttpGet("")]
        public IEnumerable<Place> Get()
        {
            var places = new List<Place>();

            return places;
        }

        
    }
}

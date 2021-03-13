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
        public PlacesSummary Get()
        {
            var places = new List<Place>();

            var result = new PlacesSummary(places);

            return result;
        }

        
    }
}

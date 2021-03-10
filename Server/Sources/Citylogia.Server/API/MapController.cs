using Citylogia.Server.Entityes;
using Citylogia.Server.Models;
using Microsoft.AspNetCore.Mvc;
using System.Linq;
using System.Collections.Generic;

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
            using var db = new ApplicationContext();
            var places = db.Places.ToList();

            var result = new PlacesSummary(places);

            return result;
        }

        [HttpPost("")]
        public bool Add([FromBody] PlaceInputParameters placeInputParameters)
        {
            using var db = new ApplicationContext();

            var place = new Place { Name = placeInputParameters.Name, Description = placeInputParameters.Description, Mark = placeInputParameters.Mark, TypeId = 1 };

            db.Places.Add(place);
            db.SaveChanges();

            return true;
        }
    }
}

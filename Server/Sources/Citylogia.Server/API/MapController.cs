using Citylogia.Server.Models;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;

namespace Citylogia.Server.API
{
    [ApiController]
    [Route("/api/map")]
    public class MapController : Controller
    {
        public MapController()
        {
        }

        [HttpGet]
        public IEnumerable<Coord> Get()
        {
            var coords = new List<Coord>();
            for (int i = 0; i < 10; i++)
            {
                var coord = new Coord(i, i * 2);
                coords.Add(coord);
            }

            return coords;
        }
    }
}

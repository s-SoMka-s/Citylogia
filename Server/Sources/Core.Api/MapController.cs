using Microsoft.AspNetCore.Mvc;

namespace Citylogia.Server.Core.Api
{
    [ApiController]
    [Route("/api/Map/Places")]
    public class MapController : Controller
    {
        public MapController()
        {
        }

        [HttpGet("")]
        public bool Get()
        {
            return true;
        }

        
    }
}

using Citylogia.Server.Core.Db.Implementations;
using Citylogia.Server.Core.Entityes;
using Core.Api.Models;
using Core.Api.Models.Input;
using Core.Api.Models.Output;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Linq;

namespace Citylogia.Server.Core.Api
{
    [ApiController]
    [Route("/api/Map/Places")]
    public class MapController : Controller
    {
        private readonly SqlContext context;
        public MapController(SqlContext context)
        {
            this.context = context;
        }

        [HttpGet("")]
        public BaseApiResponse<BaseCollectionResponse<PlaceSummary>> Get()
        {
            var places = this.context.Places.ToList();

            var summaries = new List<PlaceSummary>();

            foreach (var place in places)
            {
                var summary = new PlaceSummary(place);
                summaries.Add(summary);
            }

            var baseCollectionResponse = new BaseCollectionResponse<PlaceSummary>(summaries);

            return new BaseApiResponse<BaseCollectionResponse<PlaceSummary>>(200, baseCollectionResponse);
        }

        [HttpPost("")]
        public BaseApiResponse<bool> AddPlace([FromBody] PlaceInputParameters parameters)
        {
            var place = parameters.Build();

            this.context.Places.Add(place);
            this.context.SaveChanges();

            return new BaseApiResponse<bool>(200, true);
        }
        
        [HttpGet("{id}")]
        public BaseApiResponse<PlaceSummary> GetPlace(long id)
        {
            var place = this.context.Places.Find(id);
            var res = new PlaceSummary(place);

            return new BaseApiResponse<PlaceSummary>(200, res);
        }

        [HttpGet("Types")]
        public BaseApiResponse<BaseCollectionResponse<PlaceTypeSummary>> GetPlaceTypes()
        {
            var types = this.context.PlaceTypes.ToList();

            var typeSummaries = new List<PlaceTypeSummary>();
            foreach(var type in types)
            {
                var summary = new PlaceTypeSummary(type);
                typeSummaries.Add(summary);
            }

            var collectionResponse = new BaseCollectionResponse<PlaceTypeSummary>(typeSummaries);

            return new BaseApiResponse<BaseCollectionResponse<PlaceTypeSummary>>(200, collectionResponse);
        }

        [HttpPost("Types")]
        public BaseApiResponse<bool> AddPlaceType([FromBody] TypeInputParameters parameters)
        {
            var type = new PlaceType()
            {
                Name = parameters.Name
            };

            this.context.PlaceTypes.Add(type);
            this.context.SaveChanges();

            return new BaseApiResponse<bool>(200, true);
        }
    }
}

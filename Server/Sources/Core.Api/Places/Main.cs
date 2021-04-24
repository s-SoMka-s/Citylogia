﻿using Citylogia.Server.Core.Db.Implementations;
using Citylogia.Server.Core.Entityes;
using Core.Api.Models;
using Core.Api.Places.Models.Input;
using Core.Api.Places.Models.Output;
using Libraries.Updates;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;

namespace Citylogia.Server.Core.Api
{
    [ApiController]
    [Route("/api/Map/Places")]
    public class Main : Controller
    {
        private readonly SqlContext context;
        public Main(SqlContext context)
        {
            this.context = context;
        }

        [HttpGet("")]
        public BaseCollectionResponse<ShortPlaceSummary> Get()
        {
            var places = this.Query().ToList();

            var summaries = new List<ShortPlaceSummary>();

            foreach (var place in places)
            {
                var summary = new ShortPlaceSummary(place);
                summaries.Add(summary);
            }

            var baseCollectionResponse = new BaseCollectionResponse<ShortPlaceSummary>(summaries);

            return baseCollectionResponse;
        }

        [HttpPost("")]
        public bool AddPlace([FromBody] NewPlaceParameters parameters)
        {
            var place = parameters.Build();

            this.context.Places.Add(place);
            this.context.SaveChanges();

            return true;
        }

        [HttpGet("{id}")]
        public PlaceSummary GetPlace(long id)
        {
            var place = this.Query().FirstOrDefault(p => p.Id == id);
            var res = new PlaceSummary(place);

            return res;
        }

        [HttpPut("{id}")]
        public PlaceSummary UpdatePlace(long id, [FromBody] IEnumerable<UpdateContainer> updates)
        {
            return null;
        }

        [HttpGet("Types")]
        public BaseCollectionResponse<PlaceTypeSummary> GetPlaceTypes()
        {
            var types = this.context.PlaceTypes.ToList();

            var typeSummaries = new List<PlaceTypeSummary>();
            foreach (var type in types)
            {
                var summary = new PlaceTypeSummary(type);
                typeSummaries.Add(summary);
            }

            var collectionResponse = new BaseCollectionResponse<PlaceTypeSummary>(typeSummaries);

            return collectionResponse;
        }

        [HttpPost("Types")]
        public bool AddPlaceType([FromBody] NewPlaceTypeParameters parameters)
        {
            var type = new PlaceType()
            {
                Name = parameters.Name
            };

            this.context.PlaceTypes.Add(type);
            this.context.SaveChanges();

            return true;
        }

        private IQueryable<Place> Query()
        {
            return this.context
                       .Places
                       .Include(p => p.Reviews)
                       .Include(p => p.Type);
        }
    }
}
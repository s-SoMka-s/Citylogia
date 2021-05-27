using Citylogia.Server.Core.Db.Implementations;
using Citylogia.Server.Core.Entityes;
using Core.Api;
using Core.Api.Models;
using Core.Api.Places.Models.Input;
using Core.Api.Places.Models.Output;
using Core.Entities;
using GeoCoordinatePortable;
using Libraries.Db.Reposiitory.Interfaces;
using Libraries.Updates;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Citylogia.Server.Core.Api
{
    [ApiController]
    [Route("api/Map/Places")]
    public class Main : ApiController
    {
        private readonly SqlContext context;
        private readonly ICrudRepository<Place> places;
        private readonly ICrudRepository<PlaceType> placeTypes;
        private readonly ICrudRepository<FavoritePlaceLink> links;
        private readonly ICrudRepository<User> users;

        public Main(SqlContext context, ICrudFactory factory)
        {
            this.context = context;
            this.places = factory.Get<Place>();
            this.placeTypes = factory.Get<PlaceType>();
            this.links = factory.Get<FavoritePlaceLink>();
            this.users = factory.Get<User>();
        }

        [HttpGet("")]
        public async Task<BaseCollectionResponse<ShortPlaceSummary>> GetAsync([FromQuery] PlaceSelectParameters parameters)
        {
            var p = await this.places.FindAsync(p=> p.Id == 7);

            var query = this.Query();

            if (parameters.TypeIds.Any())
            {
                query = query.Where(p => parameters.TypeIds.Contains(p.TypeId));
            }

            var places = query.ToList();
            var needDistances = false;
            if (parameters.Longtitude != default && parameters.Latitude != default && parameters.RadiusInKm != default)
            {
                needDistances = true;
                places = places.Where(p => this.IsPlaceInRange(p, parameters.Longtitude, parameters.Latitude, parameters.RadiusInKm)).ToList();
            }

            var summaries = new List<ShortPlaceSummary>();
            if (needDistances)
            {
                summaries = places.Select(p => new ShortPlaceSummary(p, this.countDistanceTo(p, parameters.Longtitude, parameters.Latitude))).ToList();
            }
            else
            {
                summaries = places.Select(p => new ShortPlaceSummary(p)).ToList();
            }
            var baseCollectionResponse = new BaseCollectionResponse<ShortPlaceSummary>(summaries);

            return baseCollectionResponse;
        }

        [HttpPost("")]
        [Authorize]
        public async Task<bool> AddPlaceAsync([FromBody] NewPlaceParameters parameters)
        {
            var userId = GetUserId();

            var place = parameters.Build();
            place.UserId = userId;
            place.IsApproved = true;

            await places.AddAsync(place);

            return true;
        }

        [HttpGet("{id}")]
        public PlaceSummary GetPlace(long id)
        {
            var place = this.Query().FirstOrDefault(p => p.Id == id);

            var favorites = this.FavoritesQuery().Where(l => l.UserId == 4).Select(l => l.PlaceId).ToHashSet<long>();
            var res = new PlaceSummary(place, favorites);

            return res;
        }

        [HttpPut("{id}")]
        public PlaceSummary UpdatePlace(long id, [FromBody] IEnumerable<UpdateContainer> updates)
        {
            return null;
        }

        [HttpDelete("{id}")]
        public async Task<bool> DeleteAsync(long id)
        {
            var place = this.Query().FirstOrDefault(p => p.Id == id);
            if (place == default)
            {
                return false;
            }

            this.context.Places.Remove(place);
            await this.context.SaveChangesAsync();

            return true;
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
        public async Task<bool> AddPlaceTypeAsync([FromBody] NewPlaceTypeParameters parameters)
        {
            var type = new PlaceType()
            {
                Name = parameters.Name
            };

            await placeTypes.AddAsync(type);

            return true;
        }

        [HttpDelete("Types/{id}")]
        public async Task<bool> DeleteTypeAsync(long id)
        {
            var type = await placeTypes.FindAsync(t => t.Id == id);
            if (type == default)
            {
                return false;
            }

            this.context.Remove(type);
            await this.context.SaveChangesAsync();

            return true;
        }

        private bool IsPlaceInRange(Place place, double longtitude, double latitude, double radiusInKm)
        {
            var distanceToPlaceInKm = this.countDistanceTo(place, longtitude, latitude);
            
            return distanceToPlaceInKm <= radiusInKm;
        }

        private double countDistanceTo(Place dst, double longtitude, double latitude)
        {
            var geoPlace = new GeoCoordinate(dst.Latitude, dst.Longitude);
            var geoUser = new GeoCoordinate(latitude, longtitude);

            //NB in km
            return geoUser.GetDistanceTo(geoPlace) / 1000;
        }

        private IQueryable<Place> Query()
        {
            return this.context.Places

                       .Include(p => p.Reviews)
                       .ThenInclude(r => r.Author)

                       .Include(p => p.Type)
                       .Include(p => p.Photos);
        }

        private IQueryable<FavoritePlaceLink> FavoritesQuery()
        {
            return this.context
                       .FavoritePlaceLinks
                       .Include(l => l.User)
                       .Include(l => l.Place);
        }
    }
}

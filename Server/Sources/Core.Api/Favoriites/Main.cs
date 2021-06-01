using Citylogia.Server.Core.Db.Implementations;
using Citylogia.Server.Core.Entityes;
using Core.Api.Favoriites.Models.Input;
using Core.Api.Favoriites.Models.Output;
using Core.Api.Models;
using Core.Entities;
using Libraries.Db.Reposiitory.Interfaces;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Linq;
using System.Threading.Tasks;

namespace Core.Api.Favoriites
{
    [ApiController]
    [Route("/api/Favorites")]
    public class Main : ApiController
    {
        private readonly ICrudRepository<FavoritePlaceLink> links;
        private readonly ICrudRepository<User> users;
        private readonly ICrudRepository<Place> places;

        public Main(ICrudFactory factory)
        {
            this.links = factory.Get<FavoritePlaceLink>();
            this.users = factory.Get<User>();
            this.places = factory.Get<Place>();
        }


        [HttpGet("")]
        [Authorize]
        public async Task<BaseCollectionResponse<FavoriteSummary>> GetFavoritesAsync()
        {
            var userId = GetUserId();

            var summaries = await Query().Where(l => l.UserId == userId)
                                         .Select(l => new FavoriteSummary(l))
                                         .ToListAsync();

            return new BaseCollectionResponse<FavoriteSummary>(summaries);
        }

        [HttpPost("")]
        [Authorize]
        public async Task<bool> AddAsync([FromBody] NewFavoritePlaceLinkParameters parameters)
        {
            var userId = GetUserId();

            var @new = parameters.Build();
            var place = await places.FindAsync(p => p.Id == parameters.PlaceId);

            if (default == place)
            {
                return false;
            }

            var user = await users.FindAsync(u => u.Id == userId);

            if (default == user)
            {
                return false;
            }

            @new.UserId = userId;
            @new.PlaceId = parameters.PlaceId;

            var existed = await links.FindAsync(l => l.PlaceId == place.Id && l.UserId == user.Id);

            if (existed != default)
            {
                return false;
            }

            await links.AddAsync(@new);

            return true;
        }


        [HttpDelete("{id}")]
        [Authorize]
        public async Task<bool> DeleteAsync(long id)
        {
            var link = await links.FindAsync(l => l.Id == id);

            if (link == default)
            {
                return false;
            }

            return await links.DeleteAsync(link);
        }

        [HttpDelete("")]
        [Authorize]
        public async Task<bool> DeleteAsync([FromBody] NewFavoritePlaceLinkParameters parameters)
        {
            var link = await links.FindAsync(l => l.PlaceId == parameters.PlaceId);

            if (link == default)
            {
                return false;
            }

            return await links.DeleteAsync(link);
        }


        private IQueryable<FavoritePlaceLink> Query()
        {
            return this.links.Query()
                             .Include(l => l.Place)
                             .ThenInclude(p => p.Type)
                             .Include(l => l.User)

                             .Include(l => l.Place)
                             .ThenInclude(p => p.Photos)
                             .ThenInclude(p => p.Photo)

                             .Include(l => l.Place)
                             .ThenInclude(p => p.Reviews);
        }
    }
}

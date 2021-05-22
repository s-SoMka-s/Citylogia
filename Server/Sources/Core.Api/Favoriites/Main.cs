using Citylogia.Server.Core.Db.Implementations;
using Core.Api.Favoriites.Models.Input;
using Core.Api.Favoriites.Models.Output;
using Core.Api.Models;
using Core.Api.Places.Models.Output;
using Core.Entities;
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
        private readonly SqlContext context;

        public Main(SqlContext context)
        {
            this.context = context;
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
            var place = await context.Places
                                     .FirstOrDefaultAsync(p => p.Id == parameters.PlaceId);

            if (default == place)
            {
                return false;
            }

            var user = await context.Users
                                    .FirstOrDefaultAsync(u => u.Id == userId);

            if (default == user)
            {
                return false;
            }

            @new.User = user;
            @new.Place = place;

            var existed = await context.FavoritePlaceLinks
                                       .FirstOrDefaultAsync(l => l.PlaceId == place.Id && l.UserId == user.Id);

            if (existed != default)
            {
                return false;
            }

            await context.AddAsync(@new);
            await context.SaveChangesAsync();

            return true;
        }


        [HttpDelete("{id}")]
        [Authorize]
        public async Task<bool> DeleteAsync(long id)
        {
            var link = await context.FavoritePlaceLinks
                                    .FirstOrDefaultAsync(l => l.Id == id);

            if (link == default)
            {
                return false;
            }

            context.Remove(link);
            await context.SaveChangesAsync();

            return true;
        }


        private IQueryable<FavoritePlaceLink> Query()
        {
            return this.context.FavoritePlaceLinks
                               .Include(l => l.Place)
                               .ThenInclude(p => p.Type)
                               .Include(l => l.User)

                               .Include(l => l.Place)
                               .ThenInclude(p => p.Photos)

                               .Include(l => l.Place)
                               .ThenInclude(p => p.Reviews);
        }
    }
}

using Citylogia.Server.Core.Db.Implementations;
using Core.Api.Favoriites.Models.Input;
using Core.Api.Models;
using Core.Api.Places.Models.Output;
using Core.Entities;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Linq;
using System.Threading.Tasks;

namespace Core.Api.Favoriites
{
    [ApiController]
    [Route("/api/Favorites")]
    public class Main : Controller
    {
        private readonly SqlContext context;

        public Main(SqlContext context)
        {
            this.context = context;
        }


        [HttpGet("")]
        public async Task<BaseCollectionResponse<ShortPlaceSummary>> GetFavoritesAsync()
        {
            var summaries = await this.Query().Select(l => new ShortPlaceSummary(l.Place)).ToListAsync();

            return new BaseCollectionResponse<ShortPlaceSummary>(summaries);
        }

        [HttpPost("")]
        public async Task<bool> AddAsync([FromBody] NewFavoritePlaceLinkParameters parameters)
        {
            var @new = parameters.Build();
            var place = await this.context.Places.FirstOrDefaultAsync(p => p.Id == parameters.PlaceId);
            if (default == place)
            {
                return false;
            }

            var user = await this.context.Users.FirstOrDefaultAsync(u => u.Id == parameters.UserId);
            if (default == user)
            {
                return false;
            }

            @new.User = user;
            @new.Place = place;

            await this.context.AddAsync(@new);
            await this.context.SaveChangesAsync();

            return true;
        }


        private IQueryable<FavoritePlaceLink> Query()
        {
            return this.context.FavoritePlaceLinks.Include(l => l.Place).ThenInclude(p => p.Type).Include(l => l.User);
        } 
    }
}

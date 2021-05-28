using Citylogia.Server.Core.Db.Implementations;
using Citylogia.Server.Core.Entityes;
using Core.Api.Profile.Models.Output;
using Core.Entities;
using Libraries.Db.Reposiitory.Interfaces;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Linq;
using System.Threading.Tasks;

namespace Core.Api.Profile
{
    [Route("api/Profile")]
    [ApiController]
    public class Main : ApiController
    {
        private readonly SqlContext context;
        private readonly ICrudRepository<User> users;
        private ICrudRepository<FavoritePlaceLink> links;

        public Main(SqlContext context, ICrudFactory factory)
        {
            this.context = context;
            this.users = factory.Get<User>();
            this.links = factory.Get<FavoritePlaceLink>();
        }


        [HttpGet("")]
        [Authorize]
        public async Task<ProfileSummary> GetProfileAsync()
        {
            var userId = GetUserId();
            var user = await Query().FirstOrDefaultAsync(u => u.Id == userId);

            if (user == default)
            {
                return null;
            }

            var favorites = FavoritesQuery().Where(p => p.UserId == userId).ToHashSet();

            return new ProfileSummary(user, favorites);
        }

        private IQueryable<User> Query()
        {
            return users.Query();
        }

        private IQueryable<FavoritePlaceLink> FavoritesQuery()
        {
            return links.Query()
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

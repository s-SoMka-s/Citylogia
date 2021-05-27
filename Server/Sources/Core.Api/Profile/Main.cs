using Citylogia.Server.Core.Db.Implementations;
using Citylogia.Server.Core.Entityes;
using Core.Api.Profile.Models.Output;
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

        public Main(SqlContext context, ICrudFactory factory)
        {
            this.context = context;
            var e = factory.Get<User>();
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

            return new ProfileSummary(user);
        }

        private IQueryable<User> Query()
        {
            return this.context.Users;
        }
    }
}

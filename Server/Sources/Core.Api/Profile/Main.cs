using Citylogia.Server.Core.Db.Implementations;
using Citylogia.Server.Core.Entityes;
using Core.Api.Profile.Models.Output;
using Core.Entities;
using Libraries.Db.Reposiitory.Interfaces;
using Libraries.GoogleStorage;
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
        private readonly ICloudStorage storage;
        private readonly ICrudRepository<User> users;
        private ICrudRepository<FavoritePlaceLink> links;
        private ICrudRepository<Photo> photos;

        public Main(SqlContext context, ICloudStorage storage, ICrudFactory factory)
        {
            this.context = context;
            this.storage = storage;
            this.users = factory.Get<User>();
            this.links = factory.Get<FavoritePlaceLink>();
            this.photos = factory.Get<Photo>();
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

        [HttpPost("Avatar")]
        [Authorize]
        public async Task<bool> SetAvatarAsync([FromBody] NewFileParameters parameters)
        {
            var userId = GetUserId();

            var res = await this.storage.UploadFileAsync(parameters.Name, parameters.Extension, parameters.Content);

            var @new = new Photo()
            {
                PublicUrl = res.PublicUrl,
                Name = res.Name
            };

            var uploadedPhoto = await photos.AddAsync(@new);

            var user = await users.FindAsync(userId);

            user.PhotoId = uploadedPhoto.Id;
            this.context.Users.Update(user);
            this.context.SaveChanges();

            return true;
        }

        private IQueryable<User> Query()
        {
            return users.Query().Include(u => u.Avatar);
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

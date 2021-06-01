using Citylogia.Server.Core.Db.Implementations;
using Citylogia.Server.Core.Entityes;
using Core.Api.Places.Models.Input;
using Core.Entities;
using Libraries.Db.Reposiitory.Interfaces;
using Libraries.GoogleStorage;
using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;

namespace Core.Api.Places
{
    [ApiController]
    [Route("api/Places/")]
    public class Admin : ApiController
    {
        private readonly ICrudRepository<Photo> photos;
        private readonly ICrudRepository<PlacePhoto> placePhotos;
        private readonly ICrudRepository<Place> places;
        private readonly SqlContext context;
        private readonly ICloudStorage storage;

        public Admin(ICrudFactory factory, ICloudStorage storage, SqlContext context)
        {
            this.photos = factory.Get<Photo>();
            this.placePhotos = factory.Get<PlacePhoto>();
            this.places = factory.Get<Place>();
            this.context = context;

            this.storage = storage;
        }


        [HttpPut("{placeId}")]
        public async Task<bool> UpdatePlaceAsync(long placeId, [FromBody] PlaceUpdateParameters parameters)
        {
            var place = await this.places.FindAsync(placeId);

            place.IsApproved = parameters.IsApproved;

            context.Places.Update(place);
            await this.context.SaveChangesAsync();

            return true;
        }

        [HttpPost("{place_id}/Photo")]
        public async Task<bool> AttachPhotoAsync(long place_id, [FromBody] NewPlacePhotoParameters parameters)
        {
            var res = await storage.UploadFileAsync(parameters.Name, parameters.Extension, parameters.Content);

            var @new = new Photo()
            {
                PublicUrl = res.PublicUrl,
                Name = res.Name
            };

            var uplouded = await photos.AddAsync(@new);

            var placePhoto = new PlacePhoto()
            {
                PhotoId = uplouded.Id,
                PlaceId = place_id,
                IsMain = parameters.IsMain
            };

            await placePhotos.AddAsync(placePhoto);

            return true;
        }
    }
}

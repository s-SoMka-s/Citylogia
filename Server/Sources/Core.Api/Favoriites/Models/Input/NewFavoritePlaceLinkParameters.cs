using Core.Entities;
using Newtonsoft.Json;

namespace Core.Api.Favoriites.Models.Input
{
    public class NewFavoritePlaceLinkParameters
    {
        [JsonProperty("place_id")]
        public long PlaceId { get; set; }

        public FavoritePlaceLink Build()
        {
            return new FavoritePlaceLink();
        }
    }
}

using Core.Entities;
using Newtonsoft.Json;
using System;

namespace Core.Api.Favoriites.Models.Input
{
    public class NewFavoritePlaceLinkParameters
    {
        [JsonProperty("place_id")]
        public long PlaceId { get; set; }

        [JsonProperty("user_id")]
        public long UserId { get; set; }

        public FavoritePlaceLink Build()
        {
            return new FavoritePlaceLink();
        }
    }
}

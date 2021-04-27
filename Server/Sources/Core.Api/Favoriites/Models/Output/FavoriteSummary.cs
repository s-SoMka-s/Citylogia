using Core.Api.Places.Models.Output;
using Core.Entities;
using Newtonsoft.Json;
using System.Collections.Generic;

namespace Core.Api.Favoriites.Models.Output
{
    public class FavoriteSummary
    {
        public FavoriteSummary(FavoritePlaceLink source)
        {
            this.Id = source.Id;
            this.Place = new PlaceSummary(source.Place, new HashSet<long>());
        }

        [JsonProperty("id")]
        public long Id { get; set; }

        [JsonProperty("place")]
        public PlaceSummary Place { get; set; }
    }
}

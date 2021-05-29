using Core.Api.Places.Models.Output;
using Core.Entities;
using Newtonsoft.Json;

namespace Core.Api.Favoriites.Models.Output
{
    public class FavoriteSummary
    {
        public FavoriteSummary(FavoritePlaceLink source)
        {
            this.Id = source.Id;
            this.Place = new ShortPlaceSummary(source.Place, true);
        }

        [JsonProperty("id")]
        public long Id { get; set; }

        [JsonProperty("place")]
        public ShortPlaceSummary Place { get; set; }
    }
}

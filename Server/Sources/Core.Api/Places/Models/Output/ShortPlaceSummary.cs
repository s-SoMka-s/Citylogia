using Citylogia.Server.Core.Entityes;
using Newtonsoft.Json;

namespace Core.Api.Places.Models.Output
{
    public class ShortPlaceSummary
    {
        public ShortPlaceSummary(Place source)
        {
            this.Id = source.Id;
            this.Mark = source.Mark;
            this.Name = source.Name;
            this.Type = new PlaceTypeSummary(source.Type);
            this.Latitude = source.Latitude;
            this.Longtitude = source.Longitude;
        }


        [JsonProperty("id")]
        public long Id { get; set; }

        [JsonProperty("mark")]
        public long Mark { get; set; }

        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("type")]
        public PlaceTypeSummary Type { get; set; }

        [JsonProperty("latitude")]
        public double Latitude { get; set; }

        [JsonProperty("longitude")]
        public double Longtitude { get; set; }

    }

}

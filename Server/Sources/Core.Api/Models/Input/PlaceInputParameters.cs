using Citylogia.Server.Core.Entityes;
using Newtonsoft.Json;

namespace Core.Api.Models.Input
{
    public class PlaceInputParameters
    {
        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("mark")]
        public long Mark { get; set; }

        [JsonProperty("description")]
        public string Description { get; set; }

        [JsonProperty("type_id")]
        public long TypeId { get; set; }

        [JsonProperty("address")]
        public string Address { get; set; }

        [JsonProperty("latitude")]
        public double Latitude { get; set; }

        [JsonProperty("longitude")]
        public double Longtitude { get; set; }

        public Place Build()
        {
            return new Place()
            {
                Mark = Mark,
                Name = Name,
                Description = Description,
                TypeId = TypeId,
                Address = Address,
                Longitude = Longtitude,
                Latitude = Latitude
            };
        }

    }
}

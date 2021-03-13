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
        public AddressInputParameters Address { get; set; }

    }
}

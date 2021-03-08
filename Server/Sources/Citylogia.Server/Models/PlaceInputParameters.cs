using Newtonsoft.Json;

namespace Citylogia.Server.Models
{
    public class PlaceInputParameters
    {
        [JsonProperty("name")]
        public string Name { get; set; }
        [JsonProperty("description")]
        public string Description { get; set; }
        [JsonProperty("mark")]
        public long Mark { get; set; }
    }
}

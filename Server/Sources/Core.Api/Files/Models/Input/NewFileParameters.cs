using Newtonsoft.Json;

namespace Core.Api
{
    public class NewFileParameters
    {
        [JsonProperty("content")]
        public string Content { get; set; }

        [JsonProperty("extension")]
        public string Extension { get; set; }

        [JsonProperty("name")]
        public string Name { get; set; }
    }
}
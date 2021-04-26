using Newtonsoft.Json;

namespace Core.Api
{
    public class NewFileParameters
    {
        [JsonProperty("content")]
        public string Content { get; set; }
    }
}
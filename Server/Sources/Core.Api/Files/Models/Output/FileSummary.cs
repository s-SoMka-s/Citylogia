using Citylogia.Server.Core.Entityes;
using Newtonsoft.Json;

namespace Core.Api
{
    public class FileSummary
    {
        public FileSummary(Photo source)
        {
            this.Id = source.Id;
            this.PublicUrl = source.PublicUrl;
        }

        [JsonProperty("id")]
        public long Id { get; set; }

        [JsonProperty("public_url")]
        public string PublicUrl { get; set; }
    }
}
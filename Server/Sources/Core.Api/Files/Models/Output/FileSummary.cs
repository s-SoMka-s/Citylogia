using Citylogia.Server.Core.Entityes;
using Newtonsoft.Json;

namespace Core.Api
{
    public class FileSummary
    {
        public FileSummary(Photo source)
        {
            this.Id = source.Id;
            this.Link = source.PublicUrl;
        }

        [JsonProperty("id")]
        public long Id { get; set; }

        [JsonProperty("link")]
        public string Link { get; set; }
    }
}
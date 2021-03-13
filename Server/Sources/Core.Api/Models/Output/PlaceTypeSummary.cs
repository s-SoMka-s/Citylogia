using Citylogia.Server.Core.Entityes;
using Newtonsoft.Json;

namespace Core.Api.Models.Output
{
    public class PlaceTypeSummary
    {
        public PlaceTypeSummary(PlaceType source)
        {
            this.Id = source.Id;
            this.Name = source.Name;
        }

        [JsonProperty("id")]
        public long Id { get; set; }

        [JsonProperty("name")]
        public string Name { get; set; }
    }
}

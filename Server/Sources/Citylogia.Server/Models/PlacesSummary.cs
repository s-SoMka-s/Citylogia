using Citylogia.Server.Entityes;
using Newtonsoft.Json;
using System.Collections.Generic;

namespace Citylogia.Server.Models
{
    public class PlacesSummary
    {
        public PlacesSummary(List<Place> places)
        {
            this.Elements = places;
            this.Count = places.Count;
        }

        [JsonProperty("count")]
        public long Count { get; set; }
        [JsonProperty("elements")]
        public List<Place> Elements { get; set; }
    }
}

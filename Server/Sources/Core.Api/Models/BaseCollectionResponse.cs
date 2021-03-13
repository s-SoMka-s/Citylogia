using Newtonsoft.Json;
using System.Collections.Generic;

namespace Core.Api.Models
{
    public class BaseCollectionResponse<T>
    {
        public BaseCollectionResponse(List<T> collection)
        {
            this.Count = collection.Count;
            this.Elements = collection;
        }

        [JsonProperty("count")]
        public long Count { get; set; }

        [JsonProperty("elements")]
        public List<T> Elements { get; set; }
    }
}

using Citylogia.Server.Core.Entityes;
using Newtonsoft.Json;
using System;

namespace Core.Api
{
    public class ReviewSummary
    {
        public ReviewSummary(Review source)
        {
            this.PublishedAt = source.PublishedAt;
            this.Mark = source.Mark;
            this.Body = source.Body;
        }

        [JsonProperty("published_at")]
        public DateTimeOffset PublishedAt { get; set; }

        [JsonProperty("mark")]
        public long Mark { get; set; }

        [JsonProperty("body")]
        public string Body { get; set; }
    }
}
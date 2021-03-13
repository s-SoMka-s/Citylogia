using Newtonsoft.Json;
using System;
using System.ComponentModel.DataAnnotations.Schema;

namespace Citylogia.Server.Core.Entityes
{
    [Table("Reviews")]
    public class Review
    {
        public Review()
        {
            this.Id = 0;
            this.Body = string.Empty;
            this.Mark = 0;
            this.PublishedAt = DateTimeOffset.Now;
            this.Author = default;
        }


        [JsonProperty("id")]
        public long Id { get; set; }
        
        [JsonProperty("body")]
        public string Body { get; set; }
        
        [JsonProperty("mark")]
        public long Mark { get; set; }

        [JsonProperty("published_at")]
        public DateTimeOffset PublishedAt { get; set; }

        [ForeignKey(nameof(User))]
        public long UserId { get; set; }
        [JsonProperty("author")]
        public User Author { get; set; }
    }
}

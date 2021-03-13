using Newtonsoft.Json;
using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Citylogia.Server.Core.Entityes
{
    [Table("Reviews")]
    public class Review
    {
        public Review()
        {
            this.Body = string.Empty;
            this.Mark = 0;
            this.PublishedAt = DateTimeOffset.Now;
            this.Author = default;
        }

        [Key]
        public long Id { get; set; }
        public string Body { get; set; }
        
        public long Mark { get; set; }

        public DateTimeOffset PublishedAt { get; set; }

        [ForeignKey(nameof(User))]
        public long UserId { get; set; }
        
        public User Author { get; set; }
    }
}

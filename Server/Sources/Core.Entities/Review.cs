using Libraries.Db.Base;
using System;
using System.ComponentModel.DataAnnotations.Schema;

namespace Citylogia.Server.Core.Entityes
{
    [Table("Reviews")]
    public class Review : BaseDataType
    {
        public Review() : base()
        {
            this.Body = string.Empty;
            this.Mark = 0;
            this.PublishedAt = DateTimeOffset.Now;
            this.Author = default;
        }

        
        public string Body { get; set; }
        
        public long Mark { get; set; }

        public DateTimeOffset PublishedAt { get; set; }

        [ForeignKey(nameof(User))]
        public long UserId { get; set; }
        
        public User Author { get; set; }
    }
}

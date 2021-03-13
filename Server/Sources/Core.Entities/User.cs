using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Citylogia.Server.Core.Entityes
{
    [Table("Users")]
    public class User
    {
        public User()
        {
            this.Name = string.Empty;
            this.Surname = string.Empty;
            this.Avatar = default;
        }

        [Key]
        public long Id { get; set; }
        public string Name { get; set; }

        public string Surname { get; set; }

        [ForeignKey(nameof(Photo))]
        public long PhotoId { get; set; }

        public Photo Avatar { get; set; }
    }
}

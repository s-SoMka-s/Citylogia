using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations.Schema;

namespace Citylogia.Server.Core.Entityes
{
    [Table("Users")]
    public class User
    {
        public User()
        {
            this.Id = 0;
            this.Name = string.Empty;
            this.Surname = string.Empty;
            this.Avatar = default;
        }


        [JsonProperty("id")]
        public long Id { get; set; }

        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("surname")]
        public string Surname { get; set; }

        [ForeignKey(nameof(Photo))]
        public long PhotoId { get; set; }

        [JsonProperty("avatar")]
        public Photo Avatar { get; set; }
    }
}

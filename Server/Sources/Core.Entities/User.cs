using Libraries.Db.Base;
using System.ComponentModel.DataAnnotations.Schema;

namespace Citylogia.Server.Core.Entityes
{
    [Table("Users")]
    public class User : BaseDataType
    {
        public User() : base()
        {
            this.Name = string.Empty;
            this.Surname = string.Empty;
            this.Email = string.Empty;
            this.Password = string.Empty;
            this.Avatar = default;
        }


        public string Name { get; set; }

        public string Surname { get; set; }

        public string Email { get; set; }

        public string Password { get; set; }

        [ForeignKey(nameof(Photo))]
        public long? PhotoId { get; set; }
        public Photo Avatar { get; set; }
    }
}

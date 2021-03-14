using Citylogia.Server.Core.Entityes;

namespace Core.Api.Models.Output
{
    public class UserSummary
    {
        public UserSummary(User source)
        {
            this.Id = source.Id;
            this.Name = source.Name;
            this.Surname = source.Surname;
            this.Email = source.Email;
            this.Avatar = source.Avatar;
        }

        public long Id { get; set; }
        public string Name { get; set; }

        public string Surname { get; set; }

        public string Email { get; set; }

        public Photo Avatar { get; set; }
    }
}

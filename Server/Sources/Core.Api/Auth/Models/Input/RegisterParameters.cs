using Citylogia.Server.Core.Entityes;
using Libraries.Auth;
using Newtonsoft.Json;

namespace Core.Api.Auth.Models.Input
{
    public class RegisterParameters
    {
        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("email")]
        public string Email { get; set; }

        [JsonProperty("password")]
        public string Password { get; set; }

        public User Build()
        {
            return new User()
            {
                Name = Name,
                Email = Email,
                Password = PasswordHandler.PasswordHash(Password)
            };
        }
    }
}

using Citylogia.Server.Core.Entityes;
using Newtonsoft.Json;

namespace Core.Api.Models.Input
{
    public class AuthParameters
    {
        [JsonProperty("Email")]
        public string Email { get; set; }

        [JsonProperty("Password")]
        public string Password { get; set; }

        public User Build()
        {
            return new User()
            {
                Email = Email,
                Password = Password,
            };
        }
    }
}

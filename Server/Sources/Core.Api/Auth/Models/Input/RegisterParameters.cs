using Citylogia.Server.Core.Entityes;
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

        public User Build(string hash)
        {
            return new User()
            {
                Name = Name,
                Email = Email,
                Password = hash
            };
        }
    }
}

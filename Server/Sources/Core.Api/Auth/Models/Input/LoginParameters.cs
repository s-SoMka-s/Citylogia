using Newtonsoft.Json;

namespace Core.Api.Auth.Models.Input
{
    public class LoginParameters
    {
        [JsonProperty("email")]
        public string Email { get; set; }

        [JsonProperty("password")]
        public string Password { get; set; }
    }
}

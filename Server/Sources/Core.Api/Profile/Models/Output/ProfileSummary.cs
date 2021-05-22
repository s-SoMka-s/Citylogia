using Citylogia.Server.Core.Entityes;
using Newtonsoft.Json;

namespace Core.Api.Profile.Models.Output
{
    public class ProfileSummary
    {
        public ProfileSummary(User source)
        {
            Name = source.Name;
        }

        [JsonProperty("Name")]
        public string Name { get; set; }
    }
}

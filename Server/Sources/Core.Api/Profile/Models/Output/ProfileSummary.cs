using Citylogia.Server.Core.Entityes;
using Core.Api.Favoriites.Models.Output;
using Core.Api.Models;
using Core.Entities;
using Newtonsoft.Json;
using System.Collections.Generic;
using System.Linq;

namespace Core.Api.Profile.Models.Output
{
    public class ProfileSummary
    {
        public ProfileSummary(User source, HashSet<FavoritePlaceLink> links)
        {
            Id = source.Id;
            Name = source.Name;
            Surname = source.Surname;
            Email = source.Email;
            if (source.Avatar != default)
            {
                Avatar = new FileSummary(source.Avatar);
            }
            else
            {
                Avatar = null;
            }

            var summaries = links.Select(p => new FavoriteSummary(p)).ToList();

            Favorites = new BaseCollectionResponse<FavoriteSummary>(summaries);
        }


        [JsonProperty("id")]
        public long Id { get; set; }

        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("surname")]
        public string Surname { get; set; }

        [JsonProperty("email")]
        public string Email { get; set; }

        [JsonProperty("avatar")]
        public FileSummary Avatar { get; set; }

        [JsonProperty("favorites")]
        public BaseCollectionResponse<FavoriteSummary> Favorites { get; set; }
    }
}

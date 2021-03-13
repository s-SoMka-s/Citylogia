using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Text;

namespace Core.Api.Models.Input
{
    public class TypeInputParameters
    {
        [JsonProperty("name")]
        public string Name { get; set; }
    }
}

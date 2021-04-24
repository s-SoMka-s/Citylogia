using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Text;

namespace Libraries.Updates
{
    public class UpdateContainer
    {
        [JsonProperty("property")]
        public string Property { get; set; }

        [JsonProperty("value")]
        public string Value { get; set; }
    }
}

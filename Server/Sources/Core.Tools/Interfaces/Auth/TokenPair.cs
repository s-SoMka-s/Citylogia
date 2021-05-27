using System;
using System.Collections.Generic;
using System.Text;

namespace Core.Tools.Interfaces.Auth
{
    public class TokenPair
    {
        public string Access { get; set; }
        public string Refresh { get; set; }

        public long AccessExpiryDate { get; set; }
        public long RefreshExpiryDate { get; set; }
    }
}

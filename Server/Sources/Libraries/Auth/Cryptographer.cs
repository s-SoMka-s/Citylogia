using System;
using System.Collections.Generic;
using System.Text;

namespace Libraries.Auth
{
    public static class Cryptographer
    {
        private static string SecretKeyString = "djksfy78s%FGDSG$YG@";
        private static int CryptKeyForChar = 214;

        public static string Crypt(string data)
        {
            if (data == null)
            {
                return string.Empty;
            }
            var t = Reverse(data);
            t += SecretKeyString;
            return ToXORAllCharacters(t);
        }

        private static string Reverse(string s)
        {
            var charArray = s.ToCharArray();
            Array.Reverse(charArray);
            return new string(charArray);
        }

        private static string ToXORAllCharacters(string data)
        {
            var result = string.Empty;
            for (var i = 0; i < data.Length; i++)
                result += (char)(CryptKeyForChar ^ data[i]);
            return result;
        }
    }
}

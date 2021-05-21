using System;
using System.Text;

namespace Libraries.Auth
{
    public static class TokenGenerator
    {
        private const string Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
        private const int tokenLength = 25;
        private static readonly Random random = new Random(Environment.TickCount);

        public static string RandomString(int needLength)
        {
            var builder = new StringBuilder(needLength);

            for (var i = 0; i < needLength; ++i)
            {
                builder.Append(Chars[random.Next(Chars.Length)]);
            }

            return builder.ToString();
        }
    }
}

using Citylogia.Server.Core.Tools.Interfaces.AppSettings;
using Core.Tools.Interfaces.Auth;
using Core.Tools.Types;
using Libraries.Time;
using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace Core.Tools.Implementations.Auth
{
    public class JwtManager : IJwtManager
    {
        private readonly string issuer;
        private readonly SymmetricSecurityKey key;
        private readonly SigningCredentials credentials;

        private readonly int accessTokenLifetime;
        private readonly int refreshTokenLifetime;
        private DateTime DefaultNotBefore => DateTime.Now.AddSeconds(-5);

        public JwtManager(IAppSettings settings)
        {
            issuer = settings.AuthParameters.Issuer;
            accessTokenLifetime = settings.AuthParameters.AccessTokenLifetimeInMinutes;
            refreshTokenLifetime = settings.AuthParameters.RefreshTokenLifetimeInMinutes;



            var keyBytes = Encoding.ASCII.GetBytes(settings.AuthParameters.Key);
            key = new SymmetricSecurityKey(keyBytes);
            credentials = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);
        }


        async Task<TokenPair> IJwtManager.GeneratePairAsync(long userId)
        {
            // access token
            var accessJti = Guid.NewGuid();
            var accessJtiValue = accessJti.ToString();
            var accessNotBefore = DefaultNotBefore;
            var accessExpiryDate = DateTime.Now.AddMinutes(accessTokenLifetime);

            // refsresh token
            var refreshJti = Guid.NewGuid();
            var refreshJtiValue = refreshJti.ToString();
            var refreshNotBefore = DefaultNotBefore;
            var refreshExpiryDate = DateTime.Now.AddMinutes(refreshTokenLifetime);

            var accessClaims = PrepareClaims(TokenTypes.Access, userId, accessJtiValue);

            // token id from my db
            // accessClaims.Add(new Claim(JwtCustomClaimNames.TokenId, saved.Id.ToString()));

            // accessClaims.Add(new Claim(JwtCustomClaimNames.RefreshExp, refreshExpiryDate.ToTimestamp().ToString()));

            var access = new JwtSecurityToken(
                issuer: issuer,
                claims: accessClaims,
                notBefore: accessNotBefore,
                expires: accessExpiryDate,
                signingCredentials: credentials);

            var accessTokenValue = new JwtSecurityTokenHandler().WriteToken(access);

            // prepare refresh token

            // var refreshTokenValue = new JwtSecurityTokenHandler().WriteToken(refresh);

            return new TokenPair()
            {
                Access = accessTokenValue,
                Refresh = accessTokenValue,

                AccessExpiryDate = accessExpiryDate.ToTimestamp(),
                RefreshExpiryDate = refreshExpiryDate.ToTimestamp()
            };
        }

        private List<Claim> PrepareClaims(string type, long userId, string tokenId)
        {
            return new List<Claim>()
            {
                new Claim(JwtRegisteredClaimNames.Sub, userId.ToString()),
                new Claim(JwtRegisteredClaimNames.Typ, type),
                new Claim(JwtRegisteredClaimNames.Jti, tokenId),
            };
        }
    }
}

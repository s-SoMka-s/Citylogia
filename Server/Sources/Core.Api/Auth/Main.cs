using Citylogia.Server.Core.Db.Implementations;
using Core.Api.Auth.Models.Input;
using Core.Api.Auth.Models.Output;
using Core.Api.Helpers;
using Core.Api.Services;
using Microsoft.AspNetCore.Cryptography.KeyDerivation;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Linq;
using System.Security.Cryptography;
using System.Threading.Tasks;

namespace Core.Api.Auth
{
    [ApiController]
    [Route("/api/Auth")]
    public class Main : Controller
    {
        private readonly SqlContext context;
        private readonly IUserService userService;

        public Main(SqlContext context, IUserService userService)
        {
            this.context = context;
            this.userService = userService;
        }


        [HttpPost("Email")]
        public async Task<AuthenticateResponse> AuthenticateAsync([FromBody] LoginParameters parameters)
        {
            var res = await this.userService.Authenticate(parameters);

            if (res == null)
            {
                return null;
            }

            return res;
        }

        [HttpPost("Register")]
        public async Task<Token> RegisterAsync([FromBody] RegisterParameters parameters)
        {
            var existed = this.context.Users.ToList().Any(u => u.Email == parameters.Email);
            if (existed)
            {
                return null;
            }

            var hash = this.getHash(parameters.Password);

            var @new = parameters.Build(hash);

            var users = this.context.Users;
            await users.AddAsync(@new);
            await this.context.SaveChangesAsync();

            return new Token(hash);
        }

        [HttpGet("Test")]
        [Authorize]
        public bool Test()
        {
            var user = this.HttpContext.Items["User"];

            return true;
        }
        private string getHash(string password)
        {
            byte[] salt = new byte[128 / 8];
            using (var rng = RandomNumberGenerator.Create())
            {
                rng.GetBytes(salt);
            }

            return Convert.ToBase64String(KeyDerivation.Pbkdf2(
                password: password,
                salt: salt,
                prf: KeyDerivationPrf.HMACSHA1,
                iterationCount: 10000,
                numBytesRequested: 256 / 8));
        }
    }
}

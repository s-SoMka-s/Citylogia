﻿using Citylogia.Server.Core.Db.Implementations;
using Core.Api.Auth.Models.Input;
using Core.Api.Auth.Models.Output;
using Core.Api.Models;
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

        public Main(SqlContext context)
        {
            this.context = context;
        }


        [HttpPost("Email")]
        public BaseApiResponse<Token> Login([FromBody] LoginParameters parameters)
        {
            var existed = this.context
                              .Users
                              .ToList()
                              .Find(u => u.Email == parameters.Email);

            if (existed == default)
            {
                return new BaseApiResponse<Token>(400, null);
            }

            var hash = this.getHash(parameters.Password);
            if (existed.Password != hash)
            {
                return new BaseApiResponse<Token>(400, null);
            }

            return new BaseApiResponse<Token>(200, new Token("121212"));
        }

        [HttpPost("Register")]
        public async Task<BaseApiResponse<Token>> RegisterAsync([FromBody] RegisterParameters parameters)
        {
            var existed = this.context.Users.ToList().Any(u => u.Email == parameters.Email);
            if (existed)
            {
                return new BaseApiResponse<Token>(400, null);
            }

            var hash = this.getHash(parameters.Password);

            var @new = parameters.Build(hash);

            var users = this.context.Users;
            await users.AddAsync(@new);
            await this.context.SaveChangesAsync();

            return new BaseApiResponse<Token>(200, new Token(hash));
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

using Citylogia.Server.Core.Db.Implementations;
using Citylogia.Server.Core.Entityes;
using Core.Api.Auth.Models.Input;
using Core.Api.Auth.Models.Output;
using Core.Api.Helpers;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace Core.Api.Services
{
    public class UserService : IUserService
    {
        private readonly SqlContext context;
        private readonly IConfiguration configuration;

        public UserService(SqlContext context, IConfiguration configuration)
        {
            this.context = context;
            this.configuration = configuration;
        }

        async Task<AuthenticateResponse> IUserService.Authenticate(LoginParameters parameters)
        {
            // try to get user from DB
            var existed = await this.context.Users.FirstOrDefaultAsync(u => u.Email == parameters.Email);

            if (existed == default)
            {
                return null;
            }

            // generate token
            var token = configuration.GenerateJwtToken(existed);

            return new AuthenticateResponse(token);
        }

        async Task<User> IUserService.GetByIdAsync(int userId)
        {
            return await this.context.Users.FirstOrDefaultAsync(u => u.Id == userId);
        }
    }
}
